package flyingperson.ecaa.block.accelerator.t4;

import flyingperson.ecaa.block.accelerator.AcceleratorBlock;
import flyingperson.ecaa.block.accelerator.AcceleratorTileEntity;
import net.minecraft.tileentity.TileEntity;

public class Acceleratort4Block extends AcceleratorBlock {

    public Acceleratort4Block() {
        super("acceleratort4");
    }

    @Override
    public AcceleratorTileEntity createNewTileEntity() {
        return new Acceleratort4TileEntity();
    }
}
