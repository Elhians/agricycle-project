package com.agricycle.app.domain;

import static com.agricycle.app.domain.MediaTestSamples.*;
import static com.agricycle.app.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Media.class);
        Media media1 = getMediaSample1();
        Media media2 = new Media();
        assertThat(media1).isNotEqualTo(media2);

        media2.setId(media1.getId());
        assertThat(media1).isEqualTo(media2);

        media2 = getMediaSample2();
        assertThat(media1).isNotEqualTo(media2);
    }

    @Test
    void postTest() {
        Media media = getMediaRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        media.setPost(postBack);
        assertThat(media.getPost()).isEqualTo(postBack);

        media.post(null);
        assertThat(media.getPost()).isNull();
    }
}
