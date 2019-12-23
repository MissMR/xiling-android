package com.xiling.ddui.bean;

/**
 * created by Jigsaw at 2018/10/26
 */
public class TopicFollowChangeEvent {
    public boolean isFollowTopic;
    public String topicId;

    public TopicFollowChangeEvent(boolean isFollowTopic, String topicId) {
        this.isFollowTopic = isFollowTopic;
        this.topicId = topicId;
    }

    public TopicFollowChangeEvent(boolean isFollowTopic) {
        this.isFollowTopic = isFollowTopic;
    }
}
