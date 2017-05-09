package net.azstudio.groooseller.model.app;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class New {

    private String content, link, title, background_color;

    public New(String content, String link, String title, String background_color) {
        this.content = content;
        this.link = link;
        this.title = title;
        this.background_color = background_color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    @Override
    public String toString() {
        return "News{" +
                "content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", background_color='" + background_color + '\'' +
                '}';
    }
}
