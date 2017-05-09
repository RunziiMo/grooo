package net.azstudio.groooseller.model.http;

/**
 * Created by runzii on 16-6-1.
 */
public class UpdateInfo {


    /**
     * name : 咕噜卖家
     * version : 15
     * changelog :
     * updated_at : 1464764564
     * versionShort : 4.0.3
     * build : 15
     * installUrl : http://download.fir.im/v2/app/install/574e887f00fc7428cb000012?download_token=61a1e51b124bcc0ef070a9771842491b
     * install_url : http://download.fir.im/v2/app/install/574e887f00fc7428cb000012?download_token=61a1e51b124bcc0ef070a9771842491b
     * direct_install_url : http://download.fir.im/v2/app/install/574e887f00fc7428cb000012?download_token=61a1e51b124bcc0ef070a9771842491b
     * update_url : http://fir.im/tcj9
     * binary : {"fsize":4925686}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private int build;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    /**
     * fsize : 4925686
     */

    private BinaryBean binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean {
        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }
    }
}
