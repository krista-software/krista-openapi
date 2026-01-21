import { useEffect, useState, forwardRef } from 'react';
import { FaCloudUploadAlt } from 'react-icons/fa';
import { TailSpin } from 'react-loader-spinner';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import './Home.css';

const states = {
  loading: 'loading',
  upload: 'upload',
  specview: 'specview',
};

const Alert = forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

function isJson(str) {
  try {
    JSON.parse(str);
  } catch (e) {
    return false;
  }
  return true;
}

export default function Home() {
  const defaultErrorMessage = 'Something went wrong. Please try again';
  const invalidJsonError = 'Incorrect json file.';
  const [current, setCurrent] = useState(states.loading);
  const [spec, setSpec] = useState(null);
  const [open, setOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState(defaultErrorMessage);

  function triggerRender() {
    setCurrent(states.loading);
    getStatus((response) => {
      const status = response.status;
      if (status === 'available') {
        getSpec((spec) => {
          setSpec(modifySpec(spec));
          setCurrent(states.specview);
        });
      } else if (status === 'unavailable') {
        setCurrent(states.upload);
      } else {
        console.log('invalid status');
      }
    });
  }

  function setError(message) {
    setErrorMessage(message || defaultErrorMessage);
    setOpen(true);
  }

  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(triggerRender, []);

  function getStatus(onDone) {
    fetch('api/spec-status', {
      method: 'GET',
    }).then((response) => {
      handleError(response);
      response.json().then((json) => onDone(json));
    });
  }

  function getSpec(onDone) {
    fetch('api/spec-view', {
      method: 'GET',
    }).then((response) => {
      handleError(response);
      response.json().then((json) => onDone(json));
    });
  }

  function handleError(response) {
    if (response.status !== 200) {
      if (response.status === 400) {
        response.json().then((errorResponse) => {
          setError(errorResponse.errorMessage);
          console.error(errorResponse.stackTrace);
        });
      } else {
        response.text().then((t) => console.error(t));
        setError();
      }
      return true;
    }
    return false;
  }

  function uploadSpec() {
    uploadFile((file) => {
      setCurrent(states.loading);
      file.text().then((text) => {
        if (!isJson(text)) {
          setError(invalidJsonError);
          triggerRender();
          return;
        }
        fetch('api/spec', {
          headers: {
            'Content-Type': 'application/json',
          },
          method: 'POST',
          body: text,
        }).then((response) => {
          const isError = handleError(response);
          if (!isError) {
            const message = { messageType: "dynamicRequestsChanged" };
            window.parent.postMessage(JSON.stringify(message), '*');
          }
          triggerRender();
        });
      });
    });
  }

  function handleClose(_, reason) {
    if (reason === 'clickaway') {
      return;
    }
    setOpen(false);
  }

  return (
    <main
      className={`flex min-h-screen flex-col bg-white items-center justify-between pl-40 pr-40 pt-4 pb-4`}
    >
      {current === states.upload && (
        <div className="w-2/3 h-24">
          <UploadPhase onClick={uploadSpec}></UploadPhase>
        </div>
      )}

      {current === states.specview && (
        <div className="w-full">
          <SpecView spec={spec}></SpecView>
        </div>
      )}

      {current === states.loading && (
        <div className="w-full h-screen flex items-center justify-center">
          <TailSpin
            height="180"
            width="180"
            color="#4fa94d"
            ariaLabel="tail-spin-loading"
            radius="1"
            wrapperStyle={{}}
            wrapperClass=""
            visible={true}
          />
        </div>
      )}

      <Snackbar
        open={open}
        autoHideDuration={2000}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
      >
        <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
          {errorMessage}
        </Alert>
      </Snackbar>
    </main>
  );
}

function modifySpec(spec) {
  spec.info.description = spec.info.description || '';
  spec.methods.map((item) => {
    item.description = item.description || '';
    return item;
  });

  return spec;
}

function SpecView({ spec }) {
  const info = spec.info;
  const methods = spec.methods;
  const methodslist = methods.map((item) => (
    <div className="flex flex-col bg-sky-800 bg-opacity-5 p-5" key={item.requestId}>
      <div className="text-base font-medium">{item.requestName}</div>
      <div className="flex flex-row items-center justify-between opacity-50 text-sm font-normal">
        <div className="w-80">{`Endpoint : ${item.endpoint}`}</div>
        <div className="w-40">{`Http method : ${item.httpMethod}`}</div>
        <div
          className="w-80 inner-html"
          dangerouslySetInnerHTML={{
            __html: item.description.replace(/href/g, "target='_blank' href"),
          }}
        ></div>
      </div>
    </div>
  ));

  return (
    <>
      <div className="flex flex-col pl-5 pr-5 pt-2 pb-2 bg-white shadow-md">
        <div>
          <div className="font-normal text-base">{info.title}</div>
          <div
            className="font-light text-sm opacity-60 inner-html"
            dangerouslySetInnerHTML={{
              __html: info.description.replace(/href/g, "target='_blank' href"),
            }}
          ></div>
        </div>
        <hr className="mt-2"></hr>
        <div className="font-light text-sm">
          <div>{`version ${info.version}`}</div>
        </div>
      </div>

      <div className="mt-4 flex flex-col gap-2">{methodslist}</div>
    </>
  );
}

function uploadFile(processFile) {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'application/JSON';
  input.onchange = (e) => {
    const file = e.target.files[0];
    processFile(file);
  };
  input.click();
}

function UploadPhase({ onClick }) {
  return <UploadButton onClick={onClick}></UploadButton>;
}

function UploadButton({ onClick }) {
  return (
    <div
      onClick={onClick}
      className="flex rounded-lg flex-col items-center bg-sky-800 bg-opacity-10 hover:cursor-pointer w-full h-25"
    >
      <div className="opacity-40">
        <FaCloudUploadAlt size={70} color="05418D" />
      </div>
      <div className="text-indigo-950">Upload Open API spec here</div>
    </div>
  );
}
