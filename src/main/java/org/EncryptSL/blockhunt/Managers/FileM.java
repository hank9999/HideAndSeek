package org.EncryptSL.blockhunt.Managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileM {
	/*
	 * Made by @author Steffion, � 2013.
	 */

	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
				// Bukkit.broadcastMessage("Directory copied from " + src
				// + "  to " + dest);
			}
			String[] files = src.list();

			if (files != null) {
				for (String file : files) {
					File srcFile = new File(src, file);
					File destFile = new File(dest, file);
					if (!srcFile.getName().equals("uid.dat")) {
						copyFolder(srcFile, destFile);
					}
				}
			}

		} else {
			InputStream in = Files.newInputStream(src.toPath());
			OutputStream out = Files.newOutputStream(dest.toPath());

			byte[] buffer = new byte[1024];

			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			// Bukkit.broadcastMessage("File copied from " + src + " to " +
			// dest);
		}
	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory()) {
			String[] lst = file.list();
			if (lst != null && lst.length == 0) {
				file.delete();
			} else {
				String[] files = file.list();

				if (files != null) {
					for (String temp : files) {
						File fileDelete = new File(file, temp);
						delete(fileDelete);
					}
				}

				String[] lst2 = file.list();
				if (lst2 != null && lst2.length == 0) {
					file.delete();
				}
			}
		} else {
			file.delete();
		}
	}
}
