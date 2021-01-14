package edu.fbs.magset.export.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {

	public static void unzip(InputStream sourceInput, String destinationPath) throws IOException {
		Path destFolderPath = Paths.get(destinationPath);

		File destDir = new File(destinationPath);
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(sourceInput);
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			if (zipEntry.isDirectory()) {
				Path entryPath = destFolderPath.resolve(zipEntry.getName());
				Files.createDirectories(entryPath);
				zipEntry = zis.getNextEntry();
				continue;
			}
			File newFile = newFile(destDir, zipEntry);
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
}