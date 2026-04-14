package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.structure.RecordComponentImpl;

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
