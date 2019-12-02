package com.dodomall.ddmall.ddui.config.poster;


public class PosterStyle extends PosterElement {

    private PosterElement header;
    private PosterElement name;
    private PosterElement qr;

    public PosterStyle() {
        super();
    }

    public PosterStyle(int x, int y, int width, int height) {
        super(x, y, width, height);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public PosterElement getHeader() {
        return header;
    }

    public void setHeader(PosterElement header) {
        this.header = header;
    }

    public PosterElement getName() {
        return name;
    }

    public void setName(PosterElement name) {
        this.name = name;
    }

    public PosterElement getQr() {
        return qr;
    }

    public void setQr(PosterElement qr) {
        this.qr = qr;
    }
}
