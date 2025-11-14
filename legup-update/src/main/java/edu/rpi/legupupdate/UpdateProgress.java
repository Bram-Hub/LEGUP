package edu.rpi.legupupdate;

public interface UpdateProgress {

    void setTotalDownloads(double total);

    void setCurrentDownload(double current);

    void setDescription(String description);

}
