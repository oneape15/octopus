package com.oneape.octopus.admin.service.uid;

import com.google.common.base.Preconditions;
import lombok.Getter;

/**
 * described : Allocate 64 bits for the uid(long).
 * +------------+-------------+-----------+----------------------------+
 * |    sign    | deltaSecond |  workerId | sequence(the same second ) |
 * +------------+-------------+-----------+----------------------------+
 * | fixed 1bit |   28bits    |  22bits   |           13bits           |
 * +------------+-------------+-----------+----------------------------+
 * <p>
 * Modify:
 */
@Getter
public class BitsAllocator {
    public static final int TOTAL_BITS = 1 << 6; // total 64 bits

    /**
     * Bits for [sign -> second -> workerId -> sequence]
     */
    private int signBits = 1;
    private final int timestampBits;
    private final int workerIdBits;
    private final int sequenceBits;

    /**
     * Max value for [ second, workerId, sequence ]
     */
    private final long maxDeltaSecond;
    private final long maxWorkerId;
    private final long maxSequence;

    /**
     * shift for timestamp & workerId
     */
    private final int timestampShift;
    private final int workerIdShift;


    /**
     * Constructor with timestampBits, workerIdBits, sequenceBits.
     * the highest bit used for sign, so 63 bits for timestampBits, workerIdBits, sequenceBits.
     *
     * @param timestampBits int
     * @param workerIdBits  int
     * @param sequenceBits  int
     */
    public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {

        int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
        Preconditions.checkArgument(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        // initialize bits
        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
        this.sequenceBits = sequenceBits;

        // initialize max value
        this.maxDeltaSecond = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // initialize shift
        this.timestampShift = workerIdBits + sequenceBits;
        this.workerIdShift = sequenceBits;
    }

    /**
     * Allocate bits for uid according to delta seconds & workerId & sequence.
     * <b>Note that:</b> the highest bit will always be 0 for sign
     *
     * @param deltaSecond long
     * @param workerId    long
     * @param sequence    long
     * @return long
     */
    public long allocate(long deltaSecond, long workerId, long sequence) {
        return (deltaSecond << timestampShift) | (workerId << workerIdShift) | sequence;
    }


}
