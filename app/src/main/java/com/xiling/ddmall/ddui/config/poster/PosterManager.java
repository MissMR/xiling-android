package com.xiling.ddmall.ddui.config.poster;

public class PosterManager {

    public static PosterManager self = null;

    public static PosterManager share() {
        if (self == null) {
            self = new PosterManager();
            self.buildA();
        }
        return self;
    }

    public void buildA() {
        A = new PosterStyle(0, 0, 500, 300);
        A.setHeader(new PosterElement(20, 20, 100, 100));
        A.setName(new PosterElement(120, 20, 100, 100));
        A.setQr(new PosterElement(120, 120, 100, 100));
    }

    private PosterStyle A;

    public PosterStyle getA() {
        return A;
    }
}
