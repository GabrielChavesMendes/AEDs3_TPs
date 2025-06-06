import { useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { useExtendibleHash } from "./useExtendibleHash";

export default function App() {
  const { directory, insertKey, globalDepth } = useExtendibleHash();
  const [input, setInput] = useState("");

  const uniqueBuckets = Array.from(
    new Map(directory.map(b => [b.id, b])).values()
  );

  return (
    <div className="p-6 max-w-5xl mx-auto text-white font-sans">
      <h1 className="text-3xl font-bold mb-4 text-center">Tabela Hash Extensível</h1>

      <div className="flex justify-center gap-2 mb-6">
        <input
          type="number"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          className="text-black px-2 py-1 rounded"
        />
        <button
          onClick={() => insertKey(Number(input))}
          className="bg-green-500 px-4 py-1 rounded hover:bg-green-600"
        >
          Inserir
        </button>
      </div>

      <div className="bg-gray-800 p-4 rounded mb-4">
        <h2 className="text-xl mb-2">Diretório (p: {globalDepth})</h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
          {directory.map((bucket, i) => (
            <div key={i} className="bg-gray-700 p-2 rounded text-sm">
              <div className="font-bold">[{i.toString(2).padStart(globalDepth, '0')}]</div>
              Id do Cesto: {bucket.id}
            </div>
          ))}
        </div>
      </div>

      <h2 className="text-xl mb-2">Buckets</h2>
      <div className="flex flex-wrap gap-4">
        <AnimatePresence>
          {uniqueBuckets.map((bucket) => (
            <motion.div
              key={bucket.id}
              layout
              initial={{ scale: 0.8, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.8, opacity: 0 }}
              transition={{ duration: 0.3 }}
              className="bg-blue-600 p-4 rounded shadow-md min-w-[120px]"
            >
              <div className="font-bold mb-1">Cesto {bucket.id}</div>
              <div>p': {bucket.localDepth}</div>
              <div className="mt-2">
                {bucket.keys.map((k) => (
                  <div key={k} className="bg-blue-300 text-black px-2 py-0.5 rounded my-0.5">
                    {k}
                  </div>
                ))}
              </div>
            </motion.div>
          ))}
        </AnimatePresence>
      </div>
    </div>
  );
}
