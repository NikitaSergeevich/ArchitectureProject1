public class FilterMerger extends FilterFramework {

    @Override
    public void run() {
        Frame frameA = getFrameA();
        Frame frameB = getFrameB();

        while (true) {
            if (frameA != null && frameB != null) {
                if (frameA.isEndFrame()) {
                    if (!frameB.isEndFrame()) {
                        writeNextFilterOutputPort(frameB);
                        frameB = getFrameB();
                        continue;
                    }
                }
                if (frameB.isEndFrame()) {
                    if (!frameA.isEndFrame()) {
                        writeNextFilterOutputPort(frameA);
                        frameA = getFrameA();
                        continue;
                    }
                }

                if (frameA.isEndFrame() && frameB.isEndFrame()) {
                    writeNextFilterOutputPort(frameA);
                    frameA = null;
                    frameB = null;
                    continue;
                }

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


    private Frame getFrameB() {
        return readNextFilterInputPort(1);
    }

    private Frame getFrameA() {
        return readNextFilterInputPort(0);
    }
}
