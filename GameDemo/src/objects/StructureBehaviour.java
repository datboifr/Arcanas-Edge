package objects;

import java.sql.Struct;

public interface StructureBehaviour {
    void created(Structure structure);

    void update(Structure structure);

    void hit(Struct structure);
}
