package de.loteslab.mmm.sqlc.lang;

import java.io.File;

public class Script {
	private File file;
	private String content;
	
	public Script(File file, String content) {
		this.file = file;
		this.content = content;
	}

	public File getFile() {
		return file;
	}
	
	public String getContent() {
		return content;
	}
}
