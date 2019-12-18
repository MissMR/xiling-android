package com.xiling.ddmall.module.community;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/4/18  10:06
 * @desc ${TODD}
 */

public class CourseSection extends SectionEntity<Course> {

    public ArrayList<Course> videoList;
    public  boolean isShowMore;
    public CourseModule.CategoryBean category;

    public CourseSection(boolean isHeader, String header, CourseModule.CategoryBean category, boolean isShowMore) {
        super(isHeader, header);
        this.category = category;
        this.isShowMore = isShowMore;
    }

    public CourseSection(Course t) {
        super(t);
    }


    public CourseSection(ArrayList<Course> courseList) {
        super(false,"");
        this.videoList=courseList;
    }
}
