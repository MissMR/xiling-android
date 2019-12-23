package com.xiling.module.publish;

import java.util.List;

/**
 * @author Stone
 * @time 2018/4/13  14:15
 * @desc ${TODD}
 */

public class PublishInfoBody  {

    /**
     * materialLibrary : {"type":1,"content":"app客户端图文消息发布","images":["http://39.108.50.110/G1/M00/00/29/rBIdXFqnrmOAdmPSAAHg3EJpZII297.jpg","http://39.108.50.110/G1/M00/00/29/rBIdXFqnrmCAFjYBAAJEE_4-fGA003.jpg"],"mediaImage":"","mediaTitle":"","mediaUrl":""}
     * materialLibraryCategory : [{"categoryId":"39dbee51ff21432ba57cb0cf7c64037e"},{"categoryId":"60d22a2438f44d189758b42c9c109e8a"}]
     */

    private MaterialLibraryBean materialLibrary;
    private List<MaterialLibraryCategoryBean> materialLibraryCategory;

    public MaterialLibraryBean getMaterialLibrary() {
        return materialLibrary;
    }

    public void setMaterialLibrary(MaterialLibraryBean materialLibrary) {
        this.materialLibrary = materialLibrary;
    }

    public List<MaterialLibraryCategoryBean> getMaterialLibraryCategory() {
        return materialLibraryCategory;
    }

    public void setMaterialLibraryCategory(List<MaterialLibraryCategoryBean> materialLibraryCategory) {
        this.materialLibraryCategory = materialLibraryCategory;
    }

    public static class MaterialLibraryBean {
        /**
         * type : 1
         * content : app客户端图文消息发布
         * images : ["http://39.108.50.110/G1/M00/00/29/rBIdXFqnrmOAdmPSAAHg3EJpZII297.jpg","http://39.108.50.110/G1/M00/00/29/rBIdXFqnrmCAFjYBAAJEE_4-fGA003.jpg"]
         * mediaImage :
         * mediaTitle :
         * mediaUrl :
         */


        private int type;
        private String content="";
        private String mediaImage="";
        private String mediaTitle="";
        private String mediaUrl="";

        public String getLibraryId() {
            return libraryId;
        }

        public void setLibraryId(String libraryId) {
            this.libraryId = libraryId;
        }

        private String libraryId;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMediaImage() {
            return mediaImage;
        }

        public void setMediaImage(String mediaImage) {
            this.mediaImage = mediaImage;
        }

        public String getMediaTitle() {
            return mediaTitle;
        }

        public void setMediaTitle(String mediaTitle) {
            this.mediaTitle = mediaTitle;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class MaterialLibraryCategoryBean {
        public MaterialLibraryCategoryBean(String categoryId) {
            this.categoryId = categoryId;
        }

        /**
         * categoryId : 39dbee51ff21432ba57cb0cf7c64037e
         */

        private String categoryId="";

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}
