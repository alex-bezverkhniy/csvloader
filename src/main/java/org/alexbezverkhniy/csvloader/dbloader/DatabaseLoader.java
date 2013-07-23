package org.alexbezverkhniy.csvloader.dbloader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * 
 * @author Alexander Bezverkhniy
 *
 */
public interface DatabaseLoader {
	public void loadData(File inputDataFile, boolean truncate) throws SQLException, ClassNotFoundException, IOException;	
}
