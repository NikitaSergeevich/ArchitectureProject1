package CommonFiles;

public class FilterMerger extends FilterFramework {

    @Override
    public void run() {
        Frame frameA = getFrameA();
        Frame frameB = getFrameB();

        while (true) {

            if (frameA != null && frameB != null) {
                if (writeAllFramesIfEndFrame(frameA, frameB)) break;

                if (frameA.getTime() < frameB.getTime()) {
                    writeNextFilterOutputPort(frameA);
                    frameA = getFrameA();
                } else {
                    writeNextFilterOutputPort(frameB);
                    frameB = getFrameB();
                }
            }
        }
    }

    private boolean writeAllFramesIfEndFrame(Frame frameA, Frame frameB) {
        if (frameA.isEndFrame()) {
            if (!frameB.isEndFrame()) {
                writeNextFilterOutputPort(frameB);
                return true;
            }
        }
        if (frameB.isEndFrame()) {
            if (!frameA.isEndFrame()) {
                writeNextFilterOutputPort(frameA);
                return true;
            }
        }
        return false;
    }

    private Frame getFrameB() {
        return readNextFilterInputPort(1);
    }

    private Frame getFrameA() {
        return readNextFilterInputPort(0);
    }
}
