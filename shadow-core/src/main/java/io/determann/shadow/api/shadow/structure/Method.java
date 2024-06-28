package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.modifier.*;

public interface Method extends Executable,
                                StaticModifiable,
                                DefaultModifiable,
                                AccessModifiable,
                                AbstractModifiable,
                                FinalModifiable,
                                StrictfpModifiable,
                                NativeModifiable
{
}
