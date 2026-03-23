package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.Spine;
import com.ebook_translator.ebook_translator.domain.model.SpineItem;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.SpineItemRefXml;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.SpineXml;

import java.util.ArrayList;
import java.util.List;

public class SpineXmlMapper {

    public Spine map(SpineXml spineXml) {
        if (spineXml == null || spineXml.getItems() == null) {
            throw new InvalidEbookFormatException();
        }

        List<SpineItem> items = new ArrayList<>();

        for (SpineItemRefXml spineItemRefXml : spineXml.getItems()) {
            String idRef = spineItemRefXml.getIdRef();

            if (idRef == null || idRef.isBlank()) {
                throw new InvalidEbookFormatException();
            }

            items.add(new SpineItem(idRef));
        }

        return new Spine(List.copyOf(items));
    }
}
