package de.douglas.maven.plugin.rpmsystemd.rpm;

public class DirectoryConfiguration {

    private String directory;
    private String user;
    private String group;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
