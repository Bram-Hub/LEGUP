package utility;

import java.io.File;
import java.io.FilenameFilter;

public class ProofFilter implements FilenameFilter
{
    private static final String FILTER = ".proof";

    /**
     * Filters for proof files
     *
     * @param file file object
     * @param name name of the file
     * @return true if the file is a proof file, false otherwise
     */
    public boolean accept(File file, String name)
    {
        if(name.contains(FILTER))
        {
            return true;
        }
        return false;
    }
}
