package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.structure.RecordComponentImpl;

import javax.lang.model.element.RecordComponentElement;

public class RecordComponentAdapter
{
   private final LM.RecordComponent recordComponent;

   RecordComponentAdapter(LM.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   public RecordComponentElement toRecordComponentElement()
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }
}
