package com.mhp.coding.challenges.mapping.mappers;

import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.Image;
import com.mhp.coding.challenges.mapping.models.db.blocks.*;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.ImageDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {
    private Map<Class<?>, Function<ArticleBlock, ArticleBlockDto>> articleBlockMapper = new HashMap<>();

    public ArticleMapper() {
        articleBlockMapper.put(GalleryBlock.class, this::galleryBlockMapper);
        articleBlockMapper.put(ImageBlock.class, this::imageBlockMapper);
        articleBlockMapper.put(TextBlock.class, this::textBlockMapper);
        articleBlockMapper.put(VideoBlock.class, this::videoBlockMapper);
    }


    public ArticleDto map(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setDescription(article.getDescription());
        articleDto.setAuthor(article.getAuthor());
        articleDto.setBlocks(article.getBlocks().stream()
                .map(this::map)
                .sorted(Comparator.comparing(ArticleBlockDto::getSortIndex))
                .collect(Collectors.toList()));
        return articleDto;
    }

    private ArticleBlockDto map(ArticleBlock articleBlock) {
        Function<ArticleBlock, ArticleBlockDto> mapper = articleBlockMapper.entrySet().stream()
                .filter(classFunctionEntry -> articleBlock.getClass().equals(classFunctionEntry.getKey()))
                .findAny().get().getValue();
        return mapper.apply(articleBlock);
    }

    private GalleryBlockDto galleryBlockMapper(ArticleBlock articleBlock) {
        GalleryBlock galleryBlock = (GalleryBlock) articleBlock;
        GalleryBlockDto blockDto = new GalleryBlockDto();
        blockDto.setSortIndex(galleryBlock.getSortIndex());
        blockDto.setImages(galleryBlock.getImages().stream().filter(Objects::nonNull)
                .map(image -> new ImageDto(image.getId(), image.getUrl(), image.getImageSize()))
                .collect(Collectors.toList()));
        return blockDto;
    }

    private com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock imageBlockMapper(ArticleBlock articleBlock) {
        ImageBlock imageBlock = (ImageBlock) articleBlock;
        Image image = imageBlock.getImage();
        com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock blockDto = new com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock();
        blockDto.setSortIndex(imageBlock.getSortIndex());
        if (image != null) {
            blockDto.setImage(new ImageDto(image.getId(), image.getUrl(), image.getImageSize()));
        }
        return blockDto;
    }

    private com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock textBlockMapper(ArticleBlock articleBlock) {
        TextBlock textBlock = (TextBlock) articleBlock;
        com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock blockDto = new com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock();
        blockDto.setSortIndex(textBlock.getSortIndex());
        blockDto.setText(textBlock.getText());
        return blockDto;
    }

    private com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock videoBlockMapper(ArticleBlock articleBlock) {
        VideoBlock videoBlock = (VideoBlock) articleBlock;
        com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock blockDto = new com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock();
        blockDto.setSortIndex(videoBlock.getSortIndex());
        blockDto.setType(videoBlock.getType());
        blockDto.setUrl(videoBlock.getUrl());
        return blockDto;
    }

    public Article map(ArticleDto articleDto) {
        // Nicht Teil dieser Challenge.
        return new Article();
    }
}
