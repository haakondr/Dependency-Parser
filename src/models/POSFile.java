package models;


public class POSFile {
	private String[] lines;
	private String relPath;
	
	public POSFile(String relPath, String[] lines) {
		this.relPath = relPath;
		this.setLines(lines);
	}

	public String[] getLines() {
		return lines;
	}

	public void setLines(String[] lines) {
		this.lines = lines;
	}

	public String getRelPath() {
		return relPath;
	}
}
