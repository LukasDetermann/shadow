package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.shadow.structure.RecordComponentImpl;

import javax.lang.model.element.RecordComponentElement;

public class RecordComponentAdapter
{
   private final Ap.RecordComponent recordComponent;

   RecordComponentAdapter(Ap.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   public RecordComponentElement toRecordComponentElement()
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }
}
