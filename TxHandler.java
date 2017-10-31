public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    UTXOPool utxoPool;

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
        // (1) Implementation begin
        ArrayList<UTXO> allUTXO = utxoPool.getAllUTXO();
        for (int i = 0; i < tx.numOutputs(), i++){
            Transaction.Output txOutput = tx.getOutput(i);
            boolean found = false;
            for (UTXO utxo : allUTXO) {
                Transaction.Output poolOutput = utxoPool.getTxOutput(utxo);
                found = (txOutput.value == poolOutput.value
                        && txOutput.address == poolOutput.address);
            }
            if (!found) {
                return false;
            }
        }

        // (1) Implementation end

        // (2) Implementation begin
        for (int i = 0; i < tx.numInputs(), i++) {
            Trasaction.Input input = tx.getInput(i);
            Transaction.Output prevTxOutput = utxoPool.getTxOutput(new UTXO(tempInput.prevTxHash, tempInput.outputIndex));
            if (prevTxOutput == null || !Crypto.verifySignature(prevTxOutput.address, prevTxOutput.value, input.signature)) {
                return false
            }
        }
        // (2) Implementation end

        // (3) Implementation begin

    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    }

}
