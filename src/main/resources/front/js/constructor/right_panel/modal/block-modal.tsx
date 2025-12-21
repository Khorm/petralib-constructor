import React, { useState, useEffect, useRef, forwardRef } from 'react';
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Autocomplete from '@mui/material/Autocomplete';
import Alert from '@mui/material/Alert';

import 'bootstrap/dist/css/bootstrap.min.css';

import VariableList from './variable/variable-list';
import { VariableData } from './variable/variable';

// Типы
interface Service {
  id: number;
  name: string;
  projectId: number;
  path: string;

}

interface Block {
  id?: number;
  name: string;
  service: Service | null;
  variables: VariableData[] | null | undefined;
  type: "WORKFLOW" | "ACTION" | "SOURCE";
  
}

interface BlockModalProps {
  block: Block;
  open: boolean;
  handleClose: () => void;
  type: "WORKFLOW" | "ACTION" | "SOURCE"
}


const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50%',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

// Основной компонент
const BlockModal = forwardRef<unknown, BlockModalProps>(({ block, open, handleClose, type }, ref) => {
  const [name, setName] = useState<string>('');
  const [variables, setVariables] = useState<VariableData[]>([]);
  const [services, setServices] = useState<Service[]>([]);
  const [service, setService] = useState<Service | null>(null);

  const [alerts, setAlerts] = useState<string>(null);

  // const variableRefs = useRef<Array<React.RefObject<unknown>>>([]);

  useEffect(() => {
    console.log("block", block);
    if (block.id !== undefined) {
      setName(block.name);
      const safeVariables: VariableData[] = Array.isArray(block.variables) ? block.variables : [];
      setVariables(safeVariables);
      setService(block.service || null);
      // variableRefs.current = safeVariables.map(() => React.createRef());
    }

    axios
      .get<Service[]>('/api/v1/service', {
        params: { projectId: getProjectId() },
      })
      .then((response) => {
        setServices(response.data);
      })
      .catch((error) => {
        alert(`Ошибка загрузки сервисов: ${error.message}`);
      });
  }, []);

  const saveBlock = () => {
    setAlerts(null);
    // const collectedVariables: VariableData[] = [];
    // variables.forEach((childRef) => {
    //   if (childRef && typeof (childRef as any).getVariable === 'function') {
    //     const variable = (childRef as any).getVariable();
    //     collectedVariables.push(variable);
    //     console.log('NAME: ' + variable.name);
    //   }
    // });

    console.log('Saved variables:', variables);
    let url;
    switch (type) {
      case 'WORKFLOW':
        url = '/api/v1/block/workflow';
        break;
      case 'ACTION':
        url = '/api/v1/block/action';
        break;
      case 'SOURCE':
        url = '/api/v1/block/source';
        break;
      default:
        url = '/api/v1/block';
    }
    axios
      .post(
        url,
        {
          id: block.id,
          name,
          service,
          variables,
          type: type          
        },
        {
          params: { projectId: getProjectId() },
        }
      )
      .then((response) => {
        console.log('OK:', response);
        handleClose();
      })
      .catch((error) => {
        console.error(error);
        
        // alert(`Ошибка сохранения: ${error.message}`);
        if (error.response?.data){
          const errors: string = error.response.data.join(" | ");
          setAlerts(errors);
        }
      });
  };

const addInVariable = () => {
  addVariable('IN');
}

const addOutVariable = () => {
  addVariable('OUT');
}


const addVariable = (pinType: 'IN' | 'OUT') => {
  const newVariable: VariableData = { 
    name: '',
    multiplicity: 'SINGLE',
    variableType: null,
    pinType,

  };
  setVariables(prev => [...prev, newVariable]);
};

const removeVariable = (index: number) => {
  setVariables(prev => prev.filter((_, i) => i !== index));
};

const editVariable = (varData: VariableData, index: number) => {
  setVariables(prev => prev.map((v, i) => i === index ? varData : v));
};

  return (
    <Modal open={open} onClose={handleClose}>
      <Box sx={style}>
        {alerts && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {alerts}
          </Alert>
        )}
        <div style={{ marginBottom: '16px' }}>
          <TextField
            fullWidth
            value={name}
            onChange={(e) => setName(e.target.value)}
            label="Название"
            variant="outlined"
          />
        </div>

        <div style={{ width: '100%', marginBottom: '16px' }}>
          <Autocomplete
            fullWidth
            options={services}
            getOptionLabel={(option) => option.name || ''}
            value={service}
            onChange={(_, newValue) => setService(newValue)}
            renderInput={(params) => <TextField {...params} label="Сервис" />}
            isOptionEqualToValue={(option, value) => option.id === value?.id}
          />
        </div>

        <VariableList
          variables={variables}
          addInVariable={addInVariable}
          addOutVariable={addOutVariable}
          removeVariable={removeVariable}
          editVariable={editVariable}
          listOwner={type}
        />

        <Button variant="outlined" onClick={saveBlock} sx={{ mt: 2 }}>
          Сохранить
        </Button>
      </Box>
    </Modal>
  );
});

BlockModal.displayName = 'BlockModal';

export default BlockModal;
// import React, { useState, useEffect, useRef } from 'react'
// import axios from 'axios';

// import Modal from '@mui/material/Modal';
// import Box from '@mui/material/Box';
// import TextField from '@mui/material/TextField';
// import Button from '@mui/material/Button';

// import 'bootstrap/dist/css/bootstrap.min.css';
// import Autocomplete from '@mui/material/Autocomplete';

// import VariableList from './variable/variable-list';

// const style = {
//   position: 'absolute',
//   top: '50%',
//   left: '50%',
//   transform: 'translate(-50%, -50%)',
//   width: '50%',
//   bgcolor: 'background.paper',
//   border: '2px solid #000',
//   boxShadow: 24,
//   p: 4,
// };

// export default function BlockModal({block, open, handleClose, url}){

//     const [name, setName] = React.useState('');
//     const [variables, setVariables] = React.useState([]);
//     const [services, setServices] = React.useState([]);
//     const [service, setService] = React.useState({});
//     const variableRefs = useRef([]);

//     useEffect(() => {
//             if (block.id !== undefined){
//                 setName(block.name);
//                 setVariables(block.variables);
//                 variableRefs.current = [...block.variables];
//                 setService(block.service);
//             }
//             axios.get('/api/v1/service',{ params: {
//                 projectId: getProjectId()
//             }})
//             .then((response) => {
//                 setServices(response.data)
//             }).catch((error) => {
//               alert(error)
//            })
//         }, [])

//     function saveBlock(){
//         let variables = [];
//         variableRefs.current.forEach((childRef) => {
//             if (childRef) {
//                 variables.push(childRef.variable.getVariable());
//                 console.log("NAME " + childRef.variable.getVariable().name);
//             }
//         });
//         console.log("Saved variables:", variables);

//         axios.post(url,{
//             id: block.id,
//             name: name,
//             service: service,
//             variables: variables
//         },{
//          params :{
//             projectId: getProjectId()
//         }}
//         ).then((response) => {
//              console.log("OK: ",response);
//              handleClose();
//          }).catch((error) => {
//             console.log(error);
//             alert(error)
//          })
//     }

//     return(
//         <Modal
//             open={open}
//             onClose={handleClose}
//           >
//             <Box sx={style}>
//                 <div><TextField value={name} onChange = {(e) => setName(e.target.value)} id="outlined-basic" label="Name" variant="outlined" /></div>
//                 <div style={{width: '30%'}}>
//                     <Autocomplete
//                         fullWidth
//                         getOptionLabel={(option) => {if (option !== undefined && option.name !== undefined) return option.name; else return ''}}
//                         value={service}
//                         options={services}
//                         onChange={(event, newVar) => setService(newVar) }
//                       renderInput={(params) => <TextField {...params} label="Service" />}
//                     />
//                 </div>
//                 <VariableList incomeVariables={variables} setVariables={setVariables} ref={variableRefs} />
//                 <Button variant="outlined" onClick={saveBlock}>Save</Button>
//             </Box>
//           </Modal>
//     )
// }