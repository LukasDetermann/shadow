package io.determann.shadow.api.lang_model;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public interface ContextImplementation
{
   Types getTypes();

   Elements getElements();
}
