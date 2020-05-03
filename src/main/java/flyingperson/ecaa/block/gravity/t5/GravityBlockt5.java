package flyingperson.ecaa.block.gravity.t5;

import flyingperson.ecaa.block.gravity.BlockGravity;
import flyingperson.ecaa.block.gravity.TileEntityGravity;

public class GravityBlockt5 extends BlockGravity {
    public GravityBlockt5() {
        super("gravityt5");
    }

    @Override
    public TileEntityGravity createNewTileEntity() {
        return new GravityTEt5();
    }
}
