import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    private UTXOPool utxoPool;

    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS

        double inputValue = 0, outputValue = 0;
        UTXOPool claimedUtxoPool = new UTXOPool();
        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input input = tx.getInput(i);
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output prevTxOutput = utxoPool.getTxOutput(utxo);
            if (prevTxOutput == null
                    || !Crypto.verifySignature(prevTxOutput.address, tx.getRawDataToSign(i), input.signature)
                    || claimedUtxoPool.contains(utxo)) {
                return false;
            }
            claimedUtxoPool.addUTXO(utxo, prevTxOutput);
            inputValue += prevTxOutput.value;
        }

        for (int i = 0; i < tx.numOutputs(); i++) {
            Transaction.Output output = tx.getOutput(i);
            if (output.value < 0) {
                return false;
            }
            outputValue += output.value;
        }

        return inputValue >= outputValue;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        Set<Transaction> acceptedTxs = new HashSet<>();

        for (Transaction tx: possibleTxs) {
            if (isValidTx(tx)) {
                acceptedTxs.add(tx);
                for (int i = 0; i < tx.numInputs(); i++) {
                    Transaction.Input input = tx.getInput(i);
                    utxoPool.removeUTXO(new UTXO(input.prevTxHash, input.outputIndex));
                }
                for (int i = 0; i < tx.numOutputs(); i++) {
                    utxoPool.addUTXO(new UTXO(tx.getHash(), i), tx.getOutput(i));
                }
            }
        }

        return acceptedTxs.toArray(new Transaction[acceptedTxs.size()]);
    }

}
