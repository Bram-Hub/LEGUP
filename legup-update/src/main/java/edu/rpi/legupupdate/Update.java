package edu.rpi.legupupdate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static edu.rpi.legupupdate.Update.Stream.CLIENT;

public class Update {

    private static final Logger logger = Logger.getLogger(Update.class.getName());

    private static final String RELEASE_CHECK_URL = "https://api.github.com/repos/jpoegs/Legup2.0/releases/latest";
    private static final String REPO_BASE_URL = "https://github.com/jpoegs/Legup2.0/releases/download/";
    private static final String MAVEN_BASE_URL = "http://central.maven.org/maven2/";
    private static final String CLIENT_LIBS_LOC = "/client/client.iml";
    private static final String LIBRARY_LINE_ID = "type=\"library\"";
    private static final Pattern LIB_PATTERN = Pattern.compile("(?<=name=\").*?(?=\")");
    private static final int UPDATE_EXIT_CODE = 52;
    private File downloadDir;
    private Stream updateStream;
    private JsonObject releaseData;
    private String updateVersion;
    private UpdateProgress progress;

    public static final String VERSION;

    static {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Update.class.getResourceAsStream("/edu/rpi/legup/VERSION")));
        String version = "UNKNOWN";
        try {
            version = reader.readLine();
            reader.close();
        } catch (IOException e) {
            logger.severe("An error occurred while attempting to read the version\n" + e.getMessage());
        }
        VERSION = version;
    }

    public Update(Stream updateStream, File updateDownloadDir) {
        Objects.requireNonNull(updateStream);
        Objects.requireNonNull(updateDownloadDir);
        this.downloadDir = updateDownloadDir;
        this.updateStream = updateStream;
    }

    public void setUpdateProgress(UpdateProgress progress) {
        this.progress = progress;
    }

    public boolean checkUpdate() {
        try {
            if (progress != null) {
                progress.setTotalDownloads(-1);
                progress.setDescription("Checking for update");
            }
            logger.info("Checking for update");
            URL releaseUrl = new URL(RELEASE_CHECK_URL);
            try (InputStream in = releaseUrl.openStream();
                 InputStreamReader reader = new InputStreamReader(in)) {
                releaseData = new JsonParser().parse(reader).getAsJsonObject();
                JsonElement tagElement = releaseData.get("tag_name");
                if (tagElement == null)
                    return false;
                updateVersion = tagElement.getAsString();
                logger.info("Current version: " + VERSION);
                logger.info("Latest version:  " + updateVersion);
                if (NetUtil.versionCompare(VERSION, updateVersion) < 0) {
                    logger.info("Update available");
                    return true;
                } else
                    logger.info("No update available");
            }
        } catch (IOException e) {
            logger.severe("Failed to check for update\n" + e.getMessage());
        }
        return false;
    }

    private String getAssetUrl(String assetName) {
        JsonArray array = releaseData.get("assets").getAsJsonArray();
        if (array == null)
            return null;
        for (int i = 0; i < array.size(); ++i) {
            JsonObject asset = array.get(i).getAsJsonObject();
            if (asset.get("name").getAsString().equals(assetName))
                return asset.get("browser_download_url").getAsString();
        }
        return null;
    }

    private void getLibs(String urlStr, HashMap<String, String> set) throws IOException {
        URL url = new URL(urlStr);
        try (InputStream is = url.openStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(LIBRARY_LINE_ID))
                    continue;
                Matcher m = LIB_PATTERN.matcher(line);
                if (!m.find())
                    continue;
                line = m.group();
                String[] split = line.split(":");
                if (split.length != 3)
                    throw new IOException("Invalid library list in remote repository");
                String groupId = split[0].replaceAll("\\.", "/");
                String artifactId = split[1];
                String version = split[2];
                String name = artifactId + "-" + version + ".jar";
                set.put(name, MAVEN_BASE_URL + groupId + "/" + artifactId + "/" + version + "/" + name);
            }
        }
    }

    private HashMap<String, String> getLibs() throws IOException {
        HashMap<String, String> libs = new HashMap<>();
        getLibs(REPO_BASE_URL + updateVersion + "/" + CLIENT.assetName, libs);
        return libs;
    }

    private boolean guessDevEnvironment() {
        return !Update.class.getResource(Update.class.getSimpleName() + ".class").toString().toLowerCase().startsWith("jar:");
    }

    private void downloadFile(String urlStr, File destination) throws IOException {
        logger.info("Downloading: " + urlStr);
        URL url = new URL(urlStr);
        FileUtils.copyURLToFile(url, destination, 10000, 10000);
    }

    public boolean update() {
        if (releaseData == null)
            return false;
        if (guessDevEnvironment()) {
            logger.warning("Legup appears to be running in a development environment so automatic updating has been disabled");
//            return false;
        }
        logger.info("Starting update");
        logger.info("Getting download list");
        if (progress != null) {
            progress.setTotalDownloads(-1);
            progress.setDescription("Starting update");
        }
        String jarUrl = getAssetUrl(updateStream.assetName);
        if (jarUrl == null)
            return false;
        try {
            HashMap<String, String> libs = getLibs();
            if (!downloadDir.exists() && !downloadDir.mkdirs()) {
                logger.warning("Failed to create temporary download directory");
                return false;
            }
            int current = 0;
            if (progress != null) {
                progress.setTotalDownloads(libs.size());
                progress.setCurrentDownload(current);
                progress.setDescription("Downloading " + updateStream.assetName);
            }
            downloadFile(jarUrl, new File(downloadDir, updateStream.assetName));
            current++;
            File libDir = new File(downloadDir, "lib");
            for (Map.Entry<String, String> lib : libs.entrySet()) {
                if (progress != null) {
                    progress.setCurrentDownload(current);
                    progress.setDescription("Downloading " + lib.getKey());
                }
                downloadFile(lib.getValue(), new File(libDir, lib.getKey()));
                current++;
            }
            if(progress != null) {
                progress.setCurrentDownload(current);
                progress.setDescription("Download complete!");
            }
            logger.info("Download complete");
            return true;
        } catch (IOException e) {
            logger.severe("Failed to update Legup\n" + e.getMessage());
            return false;
        }
    }

    private void unzipFile(File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(downloadDir, entry.getName());
                if (entry.isDirectory()) {
                    if (!entryDestination.exists() && !entryDestination.mkdirs())
                        throw new IOException("Failed to unzip file: " + file.getCanonicalPath());
                } else {
                    if (!entryDestination.getParentFile().exists() && !entryDestination.getParentFile().mkdirs())
                        throw new IOException("Failed to unzip file: " + file.getCanonicalPath());
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = new FileOutputStream(entryDestination)) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        }
    }

    public void exit() {
        System.exit(UPDATE_EXIT_CODE);
    }

    public enum Stream {
        CLIENT("Legup.jar", "client-update.zip");

        public final String assetName;
        public final String extraName;

        Stream(String assetName, String extraName) {
            this.assetName = assetName;
            this.extraName = extraName;
        }
    }

}
