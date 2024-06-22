package io.determann.shadow.api.lang_model.query;

import io.determann.shadow.api.shadow.Return;
import io.determann.shadow.api.shadow.Shadow;

public interface ReturnLangModel extends Return
{
   Shadow getType();
}
