package flyingperson.ecaa.block.gravity.t4;

import flyingperson.ecaa.block.gravity.BlockGravity;
import flyingperson.ecaa.block.gravity.TileEntityGravity;

public class GravityBlockt4 extends BlockGravity {
    public GravityBlockt4() {
        super("gravityt4");
    }

    @Override
    public TileEntityGravity createNewTileEntity() {
        return new GravityTEt4();
    }
}
