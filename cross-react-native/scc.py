import subprocess
import json

# Executar o comando `scc` e obter a saída em JSON
result = subprocess.run(['scc', '--format', 'json'], stdout=subprocess.PIPE)
output = json.loads(result.stdout)

# Inicializar contadores
total_lines = 0
native_lines = 0
js_ts_lines = 0

# Definir quais linguagens são consideradas nativas
native_languages = ['Java', 'Swift', 'Objective-C', 'Objective-C++', 'Kotlin']

# Processar a saída do `scc`
for language_info in output:
    language = language_info['Name']
    lines = language_info['Code']

    total_lines += lines

    if language in native_languages:
        native_lines += lines
    elif language in ['JavaScript', 'TypeScript']:
        js_ts_lines += lines

# Calcular as porcentagens
native_percentage = (native_lines / total_lines) * 100 if total_lines else 0
js_ts_percentage = (js_ts_lines / total_lines) * 100 if total_lines else 0

print(f"Porcentagem de código nativo: {native_percentage:.2f}%")
print(f"Porcentagem de código JavaScript/TypeScript: {js_ts_percentage:.2f}%")

