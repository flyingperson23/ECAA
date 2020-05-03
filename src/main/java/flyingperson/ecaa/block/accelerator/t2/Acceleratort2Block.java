package flyingperson.ecaa.block.accelerator.t2;

import flyingperson.ecaa.block.accelerator.AcceleratorBlock;
import flyingperson.ecaa.block.accelerator.AcceleratorTileEntity;

public class Acceleratort2Block extends AcceleratorBlock {

    public Acceleratort2Block() {
        super("acceleratort2");
    }

    @Override
    public AcceleratorTileEntity createNewTileEntity() {
        return new flyingperson.ecaa.block.accelerator.t2.Acceleratort2TileEntity();
    }
}
