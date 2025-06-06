import { useState, useRef, useEffect } from "react";

type Bucket = {
  id: number;
  keys: number[];
  localDepth: number;
};

const BUCKET_SIZE = 4;

export function useExtendibleHash() {
  const [globalDepth, setGlobalDepth] = useState(1);
  const bucketCount = useRef(0);

  function createBucket(localDepth: number): Bucket {
    return { id: bucketCount.current++, keys: [], localDepth };
  }

  const [directory, setDirectory] = useState<Bucket[]>(() => {
    const initialBucket = createBucket(1);
    return new Array(1 << 1).fill(initialBucket);
  });

  const [pendingInserts, setPendingInserts] = useState<number[]>([]);

  function hash(key: number, depth: number) {
    let hash = key % Math.pow(2, depth);
    console.log("key ", key);
    console.log("profundidade ", depth);

    console.log(hash);

    return hash
  }

  function doubleDirectory(dir: Bucket[]): Bucket[] {
    return [...dir, ...dir];
  }

  function insertKey(key: number) {
    if (pendingInserts.includes(key)) return;

    const index = hash(key, globalDepth);
    const bucket = directory[index];

    if (bucket.keys.includes(key)) return;

    if (bucket.keys.length < BUCKET_SIZE) {
      bucket.keys.push(key);
      setDirectory([...directory]);
    } else {
      setPendingInserts((old) => [...old, key]);
      splitBucket(index);
    }
  }

  function splitBucket(index: number) {
    const oldBucket = directory[index];
    const oldLocalDepth = oldBucket.localDepth;
    const newLocalDepth = oldLocalDepth + 1;

    let updatedDirectory = [...directory];

    if (newLocalDepth > globalDepth) {
      updatedDirectory = doubleDirectory(updatedDirectory);
      setGlobalDepth(newLocalDepth);
    }

    const bucket1 = createBucket(newLocalDepth);
    const bucket2 = createBucket(newLocalDepth);

    const mask = 1 << oldLocalDepth; 

    for (let i = 0; i < updatedDirectory.length; i++) {
      if (updatedDirectory[i] === oldBucket) {
        updatedDirectory[i] = (i & mask) === 0 ? bucket1 : bucket2;
      }
    }

    const allKeys = [...oldBucket.keys];
    oldBucket.keys = []; 

    allKeys.forEach((k) => {
      const newIndex = k % Math.pow(2, newLocalDepth);
      if ((newIndex & mask) === 0) {
        bucket1.keys.push(k);
      } else {
        bucket2.keys.push(k);
      }
    });

    setDirectory(updatedDirectory);
  }


  useEffect(() => {
    if (pendingInserts.length === 0) return;

    const [key, ...rest] = pendingInserts;

    setPendingInserts(rest);

    const index = hash(key, globalDepth);
    const bucket = directory[index];

    if (bucket.keys.length < BUCKET_SIZE && !bucket.keys.includes(key)) {
      bucket.keys.push(key);
      setDirectory([...directory]);
    } else {
      setPendingInserts((old) => [...old, key]);
      splitBucket(index);
    }
  }, [directory, globalDepth]);

  function removeKey(key: number) {
    const index = hash(key, globalDepth);
    const bucket = directory[index];
    bucket.keys = bucket.keys.filter((k) => k !== key);
    setDirectory([...directory]);
  }

  return { directory, insertKey, removeKey, globalDepth };
}
