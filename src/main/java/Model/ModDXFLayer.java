package Model;

import org.kabeja.dxf.DXFLayer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ModDXFLayer extends DXFLayer {

    private Hashtable entities = new Hashtable();

    @Override
    public List getDXFEntities(String var1) {
        if (!var1.equals(""))
            return this.entities.containsKey(var1) ? (ArrayList)this.entities.get(var1) : null;
        else return (ArrayList)this.entities.values();
    }
}
