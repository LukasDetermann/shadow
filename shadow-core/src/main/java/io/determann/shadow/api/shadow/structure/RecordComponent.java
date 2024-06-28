package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.type.Shadow;

public interface RecordComponent extends Shadow,
                                         Nameable,
                                         Annotationable,
                                         ModuleEnclosed
{
}
