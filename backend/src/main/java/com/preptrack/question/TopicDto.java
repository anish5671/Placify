package com.preptrack.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private Long id;
    private String name;
    private String slug;
    private String category;
    private String description;
    private String icon;
    private long totalQuestions;
    private long solvedQuestions;

    public static TopicDto fromEntity(Topic topic, long totalQuestions, long solvedQuestions) {
        return TopicDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .slug(topic.getSlug())
                .category(topic.getCategory())
                .description(topic.getDescription())
                .icon(topic.getIcon())
                .totalQuestions(totalQuestions)
                .solvedQuestions(solvedQuestions)
                .build();
    }
}
