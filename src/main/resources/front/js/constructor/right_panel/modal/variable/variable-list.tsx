import React, { useEffect, useState } from 'react';

import { Button, Box, Grid, Typography } from '@mui/material';

import Variable, { VariableData, CTypeShortDto } from './variable';
import axios from 'axios';

// Интерфейс для пропсов
interface VariablesListProps {
  variables: VariableData[];
  addInVariable: () => void;
  addOutVariable: () => void;
  removeVariable: (index: number) => void;
  editVariable: (variable: VariableData, index: number) => void;
  listOwner: 'SOURCE' | 'ACTION' | 'WORKFLOW' | 'TYPE'
}

export default function VariablesList({
  variables,
  addInVariable,
  addOutVariable,
  removeVariable,
  editVariable,
  listOwner,
}: VariablesListProps) {

  const [types, setTypes] = useState<CTypeShortDto[]>([]);
  // const [loading, setLoading] = useState<boolean>(true);

    // Фильтруем переменные
  const inVariables = variables.filter((v) => v.pinType === 'IN');
  const outVariables = variables.filter((v) => v.pinType === 'OUT');
  const hasOut = outVariables.length > 0;

  useEffect(() => {
    axios
      .get<CTypeShortDto[]>('/api/v1/type', {
        // @ts-ignore
        params: { projectId: getProjectId() },
      })
      .then((response) => {
        setTypes(response.data);
        // setError(null);
      })
      .catch((err) => {
        console.error('Ошибка загрузки типов:', err);
        // setError('Не удалось загрузить список типов. Проверьте подключение.');
      })
      .finally(() => {
        // setLoading(false);
      });
  }, []);


  return (
    <Box sx={{ mt: 3 }}>
      <Typography variant="h6" gutterBottom>
        Переменные
      </Typography>

      <Grid container spacing={3}>

        {/* Колонка IN */}
        <Grid item xs={6}>
          <Typography variant="subtitle1" fontWeight="bold" gutterBottom>
            Входные (IN)
          </Typography>
          {/* Контейнер с прокруткой */}
          <Box
            sx={{
              maxHeight: '400px', // Ограничение высоты
              overflowY: 'auto',  // Вертикальная прокрутка
              border: '1px solid #e0e0e0',
              borderRadius: 1,
              p: 1,
              bgcolor: '#f9f9f9',
            }}
          >
          {inVariables.length === 0 ? (
            <Typography variant="body2" color="text.secondary" sx={{ fontStyle: 'italic' }}>
              Нет входных переменных
            </Typography>
          ) : (
            inVariables.map((variable, index) => {
              // Находим оригинальный индекс в общем массиве
              const globalIndex = variables.indexOf(variable);
              return (
                <Variable
                  key={`in-${globalIndex}`}
                  variable={variable}
                  index={globalIndex}
                  editVariable={editVariable}
                  removeVariable={removeVariable}
                  types={types}
                />
              );
            })
          )}
          </Box>
          <Button size="small" variant="outlined" color="primary" onClick={addInVariable} sx={{ mt: 1 }}>
            Добавить IN
          </Button>
        </Grid>

        {/* Колонка OUT */}
        {listOwner !== 'TYPE' &&
        <Grid item xs={6}>
          <Typography variant="subtitle1" fontWeight="bold" gutterBottom>
            Выходные (OUT)
          </Typography>
          {/* Контейнер с прокруткой */}
            <Box
              sx={{
                maxHeight: '400px',
                overflowY: 'auto',
                border: '1px solid #e0e0e0',
                borderRadius: 1,
                p: 1,
                bgcolor: '#fff8e1',
              }}
            >
          {outVariables.length === 0 ? (
            <Typography variant="body2" color="text.secondary" sx={{ fontStyle: 'italic' }}>
              Нет выходной переменной
            </Typography>
          ) : (
            outVariables.map((variable, index) => {
              const globalIndex = variables.indexOf(variable);
              return (
                <Variable
                  key={`out-${globalIndex}`}
                  variable={variable}
                  index={globalIndex}
                  editVariable={editVariable}
                  removeVariable={removeVariable}
                  types={types}
                />
              );
            })
          )}
          </Box>

          {listOwner === 'SOURCE' &&
            <Button
              size="small"
              variant="outlined"
              color="secondary"
              onClick={addOutVariable}
              disabled={hasOut}
              sx={{ mt: 1 }}
            >
              {hasOut ? 'Только одна OUT' : 'Добавить OUT'}
            </Button>
          }

          {listOwner !== 'SOURCE' && 
            <Button
              size="small"
              variant="outlined"
              color="secondary"
              onClick={addOutVariable}              
              sx={{ mt: 1 }}
            >
              {'Добавить OUT'}
            </Button>
          }
        </Grid>
        }

      </Grid>
    </Box>
  );
}