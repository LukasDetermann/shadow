package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.structure.RecordComponentImpl;

import javax.lang.model.element.RecordComponentElement;

public class RecordComponentAdapter
{
   private final AP.RecordComponent recordComponent;

   RecordComponentAdapter(AP.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   public RecordComponentElement toRecordComponentElement()
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }
}
