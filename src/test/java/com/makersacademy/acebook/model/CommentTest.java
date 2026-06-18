package com.makersacademy.acebook.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommentTest {

    private Comment comment = new Comment("hello");

    @Test
    public void CommentHasContent() {
        assertThat(comment.getContent(), containsString("hello"));
    }

}
