package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.*;

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
