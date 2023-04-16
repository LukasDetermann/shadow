package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.RecordComponent;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class RecordImpl extends DeclaredImpl implements Record
{
   public RecordImpl(ShadowApi shadowApi, DeclaredType declaredTypeMirror)
   {
      super(shadowApi, declaredTypeMirror);
   }

   public RecordImpl(ShadowApi shadowApi, TypeElement typeElement)
   {
      super(shadowApi, typeElement);
   }

   @Override
   public RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   @Override
   public List<RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> getApi().getShadowFactory().<RecordComponent>shadowFromElement(recordComponentElement))
                         .toList();
   }

   @Override
   public Record erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().erasure(getMirror()));
   }
}
