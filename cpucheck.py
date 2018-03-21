

import psutil
import sys
from concurrent.futures import ThreadPoolExecutor


def measure_cpu(interval, pids):
    def measure(pid):
            p = psutil.Process(pid)
            return (p.name(), p.cpu_percent(interval))
    with ThreadPoolExecutor(max_workers=len(pids)) as pool:
        loads = list(zip(pids, pool.map(measure, pids)))
        total = sum(d for _, (_, d) in loads)
        print("Total CPU: %f, %s" % (total, loads))
    


def main():
    if len(sys.argv) < 3:
        print("Usage: python %s <interval> <pid> [<pid> ...]" % sys.argv[0])
        sys.exit(1)
    pids = [int(arg) for arg in sys.argv[2:]]
    measure_cpu(int(sys.argv[1]), pids)

if __name__ == '__main__':
    main()