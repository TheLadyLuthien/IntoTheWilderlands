package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public abstract class SyncableComponent<T extends SyncableComponent<T, P>, P> implements AutoSyncedComponent
{
    private final ComponentKey<T> componentKey;
    private final P provider;
    private boolean canAutoSync = true;

    public SyncableComponent(ComponentKey<T> componentKey, P provider)
    {
        this.componentKey = componentKey;
        this.provider = provider;
    }

    protected boolean sync()
    {
        if (canAutoSync)
        {
            componentKey.sync(provider);
        }
        return canAutoSync;
    }

    public void haltAutoSync()
    {
        canAutoSync = false;
    }

    public void resumeAutoSync()
    {
        canAutoSync = true;
        sync();
    }
}
