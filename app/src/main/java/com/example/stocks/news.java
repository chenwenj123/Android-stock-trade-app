package com.example.stocks;

public class news {
    private String source_name;
    private String time_ago;
    private String title_name;
    private String newsimage;
    private String summary;
    private String url;
    private String realtime;

    // Constructor
    public news(String source_name, String time_ago, String title_name, String newsimage, String summary,String url, String realtime ) {
        this.source_name = source_name;
        this.time_ago = time_ago;
        this.title_name = title_name;
        this.newsimage=newsimage;
        this.summary=summary;
        this.url=url;
        this.realtime=realtime;

    }
    // Getter and Setter
    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

   public String getNewsimage() {
        return newsimage;
    }

    public void setNewsimage(String newsimage) {
        this.newsimage = newsimage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRealtime() {
        return realtime;
    }

    public void setRealtime(String realtime) {
        this.realtime = realtime;
    }
}
