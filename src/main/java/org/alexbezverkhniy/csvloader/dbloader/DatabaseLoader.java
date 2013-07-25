package org.alexbezverkhniy.csvloader.dbloader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 
 * @author Alexander Bezverkhniy
 *
 */
public interface DatabaseLoader {
	public void loadData(File inputDataFile, boolean truncate) throws SQLException, ClassNotFoundException, IOException;	
}
