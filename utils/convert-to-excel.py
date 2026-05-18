import pandas as pd
import json
from pathlib import Path

# Rutas relativas a este script — funciona desde cualquier directorio
base = Path(__file__).parent.parent
entrada = base / "data" / "reportes-recibidos.jsonl"
salida  = base / "data" / "reportes.xlsx"

with open(entrada, encoding="utf-8") as f:
    datos = [json.loads(linea) for linea in f]

pd.json_normalize(datos).to_excel(salida, index=False)
print(f"Excel generado: {salida}")