package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.Manifest;
import com.ebook_translator.ebook_translator.domain.model.ManifestItem;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.ManifestItemXml;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.ManifestXml;

import java.util.ArrayList;
import java.util.List;

public class ManifestXmlMapper {

    public Manifest map(ManifestXml manifestXml) {
        if (manifestXml == null || manifestXml.getItems() == null) {
            throw new InvalidEbookFormatException();
        }

        List<ManifestItem> items = new ArrayList<>();

        for (ManifestItemXml manifestItemXml : manifestXml.getItems()) {
            String id = manifestItemXml.getId();

            if (id == null || id.isBlank()) {
                throw new InvalidEbookFormatException();
            }

            items.add(new ManifestItem(
                    id,
                    manifestItemXml.getHref(),
                    manifestItemXml.getMediaType()
            ));
        }

        return new Manifest(List.copyOf(items));
    }
}
