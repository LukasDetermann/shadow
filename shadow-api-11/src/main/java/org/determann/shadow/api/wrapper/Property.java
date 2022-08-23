package org.determann.shadow.api.wrapper;

import org.determann.shadow.api.ApiHolder;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Method;

/**
 * A property expects a setter when the field is not final
 */
public interface Property extends ApiHolder
{
   Field getField();

   Method getGetter();

   Method getSetter();
}
