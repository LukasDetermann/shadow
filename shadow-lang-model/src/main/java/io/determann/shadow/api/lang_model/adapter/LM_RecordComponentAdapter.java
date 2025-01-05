package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.structure.LM_RecordComponent;
import io.determann.shadow.internal.lang_model.shadow.structure.RecordComponentImpl;

import javax.lang.model.element.RecordComponentElement;

public class LM_RecordComponentAdapter
{
   private final LM_RecordComponent recordComponent;

   LM_RecordComponentAdapter(LM_RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   public RecordComponentElement toRecordComponentElement()
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }
}
