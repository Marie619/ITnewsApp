package app.muneef.itnewsapp.models;

public class Books {

    private String bookName;
    private String bookUrl;

    public Books() {
    }

    public Books(String bookName, String bookUrl) {
        this.bookName = bookName;
        this.bookUrl = bookUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }
}
