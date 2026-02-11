import React, { useState } from 'react';

import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Grid from '@mui/material/Grid';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import Collapse from '@mui/material/Collapse';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';

import { CTypeShortDto, VariableDto } from '../scenario-block-modal';


interface TempVariableInputProps {
  /** Список доступных типов для выбора */
  types: CTypeShortDto[];
  /** Обработчик сохранения переменных */
  onSave: (variables: VariableDto[]) => void;
}

/**
 * Компонент для добавления временных переменных.
 *
 * Позволяет пользователю:
 * - Вводить имя переменной
 * - Выбирать тип из списка
 * - Выбирать множественность (Single/Collection)
 * - Просматривать добавленные переменные
 * - Удалять переменные
 * - Сохранять все переменные на сервер
 *
 * @component
 * @param {TempVariableInputProps} props - Пропсы компонента
 * @example
 * <TempVariableInput
 *   types={types}
 *   onSave={(vars) => console.log(vars)}
 * />
 */
export default function TempVariableInput({ types, onSave }: TempVariableInputProps) {
  const [name, setName] = useState<string>('');
  const [type, setType] = useState<CTypeShortDto>(null);
  const [multiplicity, setMultiplicity] = useState<'SINGLE' | 'COLLECTION'>('SINGLE');
  const [variables, setVariables] = useState<VariableDto[]>([]);
  const [expanded, setExpanded] = useState<boolean>(false); // Состояние раскрытия


  // Валидация
  const isValid = name.trim() !== '' && type !== null 
    && variables.find((v) => v.name === name.trim()) === undefined;

  // Добавление новой переменной
  const addVariable = () => {
    if (!isValid) return;

    const newVar: VariableDto = {
        name: name.trim(),
        variableType: type,
        multiplicity,
        id: 0,
        pinType: 'IN'
    };

    setVariables((prev) => [...prev, newVar]);
    setName('');
    setType(null);
    setMultiplicity('SINGLE');
  };

  // Удаление переменной по индексу
  const removeVariable = (index: number) => {
    setVariables((prev) => prev.filter((_, i) => i !== index));
  };

  // Сохранение на сервер
  const handleSave = () => {
    if (variables.length === 0) {
      alert('Нет переменных для сохранения');
      return;
    }
    onSave(variables);
    setVariables([]);
  };

  return (
    <Box sx={{ mt: 3, p: 2, border: '1px solid #e0e0e0', borderRadius: 2 }}>
      {/* Заголовок с возможностью раскрытия/скрытия */}
      <Box
        component="div"
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          cursor: 'pointer',
          userSelect: 'none',
          fontWeight: 500,
          fontSize: '1.25rem',
          color: 'text.primary',
          '&:hover': {
            bgcolor: 'action.hover',
            borderRadius: 1,
          },
        }}
        onClick={() => setExpanded((prev) => !prev)}
      >
        <span>Добавить временные переменные</span>
        {expanded ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
      </Box>

        {/* Контент — скрыт или виден */}
      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <Box sx={{ mt: 2 }}>
      {/* Форма ввода */}
      <Grid container spacing={2} alignItems="center" sx={{ mb: 3 }}>
        <Grid item xs={12} sm={4}>
          <TextField
            label="Имя переменной"
            value={name}
            onChange={(e) => setName(e.target.value)}
            variant="outlined"
            size="small"
            fullWidth
            onKeyPress={(e) => e.key === 'Enter' && isValid && addVariable()}
          />
        </Grid>

        <Grid item xs={12} sm={3}>
          <Autocomplete
            options={types}
            getOptionLabel={(option) => option.name}
            value={type}
            onChange={(_, newValue) => setType(newValue)}
            renderInput={(params) => <TextField {...params} label="Тип" size="small" />}
            size="small"
            fullWidth
          />
        </Grid>

        <Grid item xs={12} sm={3}>
          <Autocomplete
            options={['SINGLE', 'COLLECTION'] as const}
            value={multiplicity}
            onChange={(_, newValue) => setMultiplicity(newValue || 'SINGLE')}
            renderInput={(params) => <TextField {...params} label="Множественность" size="small" />}
            size="small"
            fullWidth
          />
        </Grid>

        <Grid item xs={12} sm={2}>
          <Button
            variant="contained"
            color="primary"
            onClick={addVariable}
            disabled={!isValid}
            fullWidth
            size="small"
          >
            Добавить
          </Button>
        </Grid>
      </Grid>

      {/* Список добавленных переменных */}
      {variables.length > 0 ? (
        <Box sx={{ mb: 3 }}>
          <Box component="h4" sx={{ fontSize: '1rem', fontWeight: 500, mb: 1 }}>
            Добавленные переменные:
          </Box>
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
            {variables.map((variable, index) => (
              <Chip
                key={index}
                label={`${variable.name}: ${variable.variableType.name} (${variable.multiplicity})`}
                onDelete={() => removeVariable(index)}
                deleteIcon={<IconButton size="small"><DeleteIcon fontSize="small" /></IconButton>}
                variant="outlined"
                sx={{ height: 'auto', py: 0.5 }}
              />
            ))}
          </Box>
        </Box>
      ) : (
        <Box sx={{ color: 'text.secondary', fontStyle: 'italic', mb: 3 }}>
          Пока нет добавленных переменных
        </Box>
      )}

      {/* Кнопка сохранения */}
      <Button
        variant="contained"
        color="success"
        onClick={handleSave}
        disabled={variables.length === 0}
        sx={{ mt: 1 }}
      >
        Сохранить все переменные
      </Button>
      </Box>
      </Collapse>
    </Box>
  );
}